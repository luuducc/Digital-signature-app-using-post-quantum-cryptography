const mongoose = require('mongoose')

const publicKeySchema = new mongoose.Schema({
  user: {
    type: mongoose.Schema.ObjectId,
    ref: 'User',
    required: [true, 'require userId']
  },
  _id: { // uuid
    type: String,
    required: [true, 'require key uuid']
  },
  dilithiumParametersType: {
    type: String, 
    required: [true, 'require dilithium parameters type']
  },
  publicKeyString: {
    type: String, 
    required: [true, 'require public key string']
  }
})

module.exports = mongoose.model('Public Key', publicKeySchema)