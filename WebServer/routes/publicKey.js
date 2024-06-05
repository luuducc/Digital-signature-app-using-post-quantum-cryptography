const express = require('express')
const router = express.Router()

const {
  savePublicKey, findUserPublicKey
} = require('../controllers/publicKey')

const {verifyToken, verifyTokenAndUser, verifyTokenAndAdmin} = require("../controllers/verifyToken");


router.route('/:userId')
  .post(verifyTokenAndUser, savePublicKey)
  .get(verifyTokenAndUser, findUserPublicKey)


module.exports = router