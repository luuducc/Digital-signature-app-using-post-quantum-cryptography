const User = require('../models/User')
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");

let refreshTokens = [];

const registerUser = async (req, res) => {
  try {
    const salt = await bcrypt.genSalt(10);
    const hashed = await bcrypt.hash(req.body.password, salt)

    const { username, email } = req.body
    console.log(username, email)
    const user = await User.create({
      username,
      email,
      password: hashed
    })
    res.json(user)
  } catch (error) {
    res.status(500).json({ msg: error.message })
  }
}

const loginUser = async (req, res) => {
  try {  
    const { email, password } = req.body
    const user = await User.findOne({ email }) 
    if (!user) {
      return res.status(404).json({ msg: 'user not found'})
    }
    const validPassword = await bcrypt.compare(password, user.password)

    if (!validPassword) {
      return res.status(404).json({ msg: "invalid password"})
    }

    const accessToken = generateToken(user)
    const refreshToken = generateRefreshToken(user)
    refreshTokens.push(refreshToken)

    //STORE REFRESH TOKEN IN COOKIE
    res.cookie("refreshToken", refreshToken, {
      httpOnly: true,
      secure:false,
      path: "/",
      sameSite: "strict",
    });

    const { password: newPass, ...rest} = user.toObject()
    res.status(200).json({ ...rest, accessToken, refreshToken})
  } catch (error) {
    res.status(500).json({ msg: error.message})
  }
}

const generateToken = (user) => {
  const { _id, username, email, isAdmin } = user
  return jwt.sign(
    { _id, username, email, isAdmin},
    process.env.SECRET_KEY,
    { expiresIn: "1h" }
  )
}
const generateRefreshToken = (user) => {
  const { username, email, password } = user
  return jwt.sign(
    { username, email, password},
    process.env.SECRET_KEY,
    { expiresIn: "365d" }
  )
}
const requestRefreshToken = async (req, res) => {
  //Take refresh token from user
  const refreshToken = req.cookies.refreshToken;

  //Send error if token is not valid
  if (!refreshToken) {
    return res.status(401).json("You're not authenticated");
  }
  if (!refreshTokens.includes(refreshToken)) {
    return res.status(403).json("Refresh token is not valid");
  }
  jwt.verify(refreshToken, process.env.REFRESH_KEY, (err, user) => {
    if (err) {
      console.log(err);
    }
    refreshTokens = refreshTokens.filter((token) => token !== refreshToken);
    //create new access token, refresh token and send to user
    const newAccessToken = authController.generateAccessToken(user);
    const newRefreshToken = authController.generateRefreshToken(user);
    refreshTokens.push(newRefreshToken);
    res.cookie("refreshToken", refreshToken, {
      httpOnly: true,
      secure:false,
      path: "/",
      sameSite: "strict",
    });
    res.status(200).json({
      accessToken: newAccessToken,
      refreshToken: newRefreshToken,
    });
  });
}

const logoutUser = async (req, res) => {
  try {
    //Clear cookies when user logs out
    refreshTokens = refreshTokens.filter((token) => token !== req.body.token);
    res.clearCookie("refreshToken");
    res.status(200).json("Logged out successfully!");
  } catch (error) {
    res.status(500).json({ msg: error.message})
  }
}
module.exports = {
  registerUser, loginUser, requestRefreshToken, logoutUser
}