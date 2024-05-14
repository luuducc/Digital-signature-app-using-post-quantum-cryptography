const express = require('express')
const router = express.Router()

const {
  registerUser, loginUser, requestRefreshToken, logoutUser
} = require('../controllers/authentications')

router.route('/register').post(registerUser)
router.route('/login').post(loginUser)
router.route('/refresh').post(requestRefreshToken)
router.route('/logout').post(logoutUser)

module.exports = router