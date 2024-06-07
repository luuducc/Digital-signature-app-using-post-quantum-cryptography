const PublicKey = require('../models/PublicKey')

const savePublicKey = async (req, res) => {
  try {
    const userId = req.user._id
    const newPublicKey = await PublicKey.create({ user: userId, ...req.body})
    // assign isRegistered field to response
    res.status(200).json({ ...newPublicKey.toObject(), isRegistered: true})
  } catch (error) {
    res.status(500).json({ error: error.message })
  }
}

const findUserPublicKey = async (req, res) => {
  try {
    const userId = req.user._id
    const keyId = req.body._id
    const returnedPublicKey = await PublicKey.findById(keyId)

    res.json("ok")
  } catch (error) {
    res.json(error.message)
  }
}

module.exports = {
  savePublicKey, findUserPublicKey
}