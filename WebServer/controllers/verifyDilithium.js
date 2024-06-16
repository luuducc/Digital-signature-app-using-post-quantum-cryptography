const PublicKey = require('../models/PublicKey')
const verifySignature = require('../verify/verifyModule')
const Transcript = require('../models/Transcript')
const fs = require('fs')

const verifyDilithiumSignature = async (req, res) => {
  try {
    const {className, keyId, initialHashedMessage, signature, isPdfElseJson} = req.body
    const userId = req.user._id

    let updatedTranscript

    // update transcript
    if (isPdfElseJson) { // update pdf signature
      updatedTranscript = await Transcript.findOneAndUpdate(
        { className, user: userId },
        { PdfSignature: signature, isSignedPdf: true }, 
        { new: true}
      )
    } else { // update json signature
      updatedTranscript = await Transcript.findOneAndUpdate(
        { className, user: userId },
        { JsonSignature: signature, isSignedJson: true },
        { new: true }
      )
    }
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