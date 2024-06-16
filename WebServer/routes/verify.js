const express = require('express')
const router = express.Router()
const {
  verifyDilithiumSignature
} = require ('../controllers/verifyDilithium')
const {
  verifyTokenAndUser,
  verifyToken
} = require('../controllers/verifyToken')
const { verify } = require('jsonwebtoken')


router.route('/').post(verifyToken, verifyDilithiumSignature)

module.exports = router