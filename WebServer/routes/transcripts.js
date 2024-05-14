const express = require('express')
const router = express.Router()
const {
  getAllTranscripts, getTranscript, createTranscript, updateTranscript, deleteTranscript, deleteStudent
} = require('../controllers/transcripts')

const {verifyToken, verifyTokenAndUser, verifyTokenAndAdmin} = require("../controllers/verifyToken");

router.route('/:userId')
  .get(getAllTranscripts)
  .post(createTranscript)

router.route('/:className')
  .get(getTranscript)
  .patch(updateTranscript)
  .delete(deleteTranscript)

router.route('/:className/:studentId')
  .delete(deleteStudent)

module.exports = router