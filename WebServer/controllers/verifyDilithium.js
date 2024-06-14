const PublicKey = require('../models/PublicKey')
const verifySignature = require('../verify/verifyModule')
const fs = require('fs')

const verifyDilithiumSignature = async (req, res) => {
  try {
    const { keyId, initialHashedMessage, signature} = req.body
    const returnedPublicKey = await PublicKey.findById(keyId)
    const { dilithiumParametersType, publicKeyString } = returnedPublicKey

    const inputObject = {
      parameterType: dilithiumParametersType,
      publicKeyStr: publicKeyString,
      signatureString: signature,
      initialHashedMessage: initialHashedMessage
    }

    fs.writeFileSync(
      './verify/input.json',
      JSON.stringify(inputObject, null, 2),
      { flag: 'w'}
    )

    const result = await verifySignature()
    res.json({ result })

  } catch (error) {
    res.status(500).json({ error: error.message })
  }
}

module.exports = {
  verifyDilithiumSignature
}