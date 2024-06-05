const express = require('express')
const router = express.Router()
const {
  verifyDilithiumSignature
} = require ('../controllers/verifyDilithium')
const {
  verifyTokenAndUser
} = require('../controllers/verifyToken')


router.route('/:userId').post(verifyTokenAndUser, verifyDilithiumSignature)

module.exports = router