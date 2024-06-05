const PublicKey = require('../models/PublicKey')
const verifySignature = require('../verifyModule')

const verifyDilithiumSignature = async (req, res) => {
  try {
    const { keyId, initialHashedMessage, signature} = req.body
    const returnedPublicKey = await PublicKey.findById(keyId)
    const { dilithiumParameteresType, publicKeyString } = returnedPublicKey

    const verifyCommand = dilithiumParameteresType + " " + publicKeyString + " " + signature + " " + initialHashedMessage

    const result = await verifySignature(verifyCommand)

    res.json(result)

  } catch (error) {
    res.json(error.message)
  }
}

module.exports = {
  verifyDilithiumSignature
}