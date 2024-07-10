const jwt = require("jsonwebtoken");

const verifyToken = (req, res, next) => {
  //ACCESS TOKEN FROM HEADER, REFRESH TOKEN FROM COOKIE
  const token = req.headers.authorization;
  // const refreshToken = req.cookies.refreshToken;
  if (token) {
    const accessToken = token.split(" ")[1];
    jwt.verify(accessToken, process.env.ACCESS_KEY, (err, user) => {
      if (err) {
        console.log(err)
        return res.status(403).json("Token is not valid!");
      }
      // create user property for request, pass the returned user value 
      req.user = user;
      next();
    });
  } else {
    res.status(401).json("Please provide a token!!");
  }
};

const verifyTokenAndUserAuthorization = (req, res, next) => {
  verifyToken(req, res, () => {
    if (req.user.id === req.params.id || req.user.isAdmin) {
      next();
    } else {
      res.status(403).json("You're not allowed to do that 2!");
    }
  });
};

const verifyTokenAndUser = (req, res, next) => {
  verifyToken(req, res, () => {
    if (req.user._id === req.params.userId) { //verify user cần id? còn admin thì không
      next();
    } else {
      res.status(403).json("Incorrect userId!");
    }
  });
}

const verifyTokenAndAdmin = (req, res, next) => {
  verifyToken(req, res, () => {
    if (req.user.isAdmin) { //verify admin không cần id
      next();
    } else {
      res.status(403).json("You're not allowed to do that!");
    }
  });
};

module.exports = {
  verifyToken,
  verifyTokenAndUserAuthorization,
  verifyTokenAndAdmin,
  verifyTokenAndUser
};
