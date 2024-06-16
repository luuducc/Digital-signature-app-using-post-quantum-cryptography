const express = require('express')
const router = express.Router()
const {
  getAllTranscripts, getTranscript, createTranscript, updateTranscript, deleteTranscript, deleteStudent
} = require('../controllers/transcripts')

const {verifyToken, verifyTokenAndUser, verifyTokenAndAdmin} = require("../controllers/verifyToken");

router.route('/')
  .get(verifyToken, getAllTranscripts)
  .post(verifyToken, createTranscript)

router.route('/:className')
  .get(verifyToken, getTranscript)
  .patch(verifyToken, updateTranscript)
  .delete(verifyToken, deleteTranscript)

router.route('/:className/:studentId')
  .delete(verifyToken, deleteStudent)

module.exports = router