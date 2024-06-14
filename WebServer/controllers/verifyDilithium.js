const PublicKey = require('../models/PublicKey')
const verifySignature = require('../verifyModule')

const verifyDilithiumSignature = async (req, res) => {
  try {
    const { keyId, initialHashedMessage, signature} = req.body
    const returnedPublicKey = await PublicKey.findById(keyId)
    const { dilithiumParametersType, publicKeyString } = returnedPublicKey

    console.log({
      dilithiumParametersType,
      publicKeyString,
      signature,
      initialHashedMessage
    })
    const verifyCommand = dilithiumParametersType + " " + publicKeyString + " " + signature + " " + initialHashedMessage

    const result = await verifySignature(verifyCommand)

    res.json({ result })

  } catch (error) {
    res.status(500).json({ error: error.message })
  }
}

module.exports = {
  verifyDilithiumSignature
}