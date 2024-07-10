const Transcript = require('../models/Transcript')
const transcriptService = require('../services/transcriptService')

const getAllTranscripts = async (req, res) => {
  try {
    // const userId = req.user._id
    const transcripts = await transcriptService.getAllTranscripts()
    if (transcripts) res.json(transcripts)
    else throw new Error('cannot find')
  } catch (error) {
    res.json(error.message)
    console.log(error.message)
  }
}

const getTranscript = async (req, res) => {
  try {
    const {className} = req.params
    const userId = req.user._id

    const transcript = await transcriptService.getTranscript(className, userId)

    if (transcript) 
      res.json(transcript)
    else 
      res.json("cannot find that transcript")
  } catch (error) {
    res.json(error.message)
  }
} 

const createTranscript = async (req, res) => {
  try {
    const transcript = req.body
    const userId = req.user._id
    const newTranscript = await transcriptService.createTranscript(transcript, userId)
    res.json(newTranscript)
  } catch (error) {
    res.json(error.message)
  }
}

const updateTranscript = async (req, res) => {
  try {
    const { className } = req.params
    const studentGrades = req.body
    const userId = req.user._id

    const transcript = await transcriptService.updateTranscript(className, studentGrades, userId)

    res.json(transcript)

  } catch (error) {
    res.json(error.message)
  }
}

const deleteTranscript = async (req, res) => {
  try {
    const { className } = req.params

    const transcript = await Transcript.findOneAndDelete({ className: new RegExp(`^${className}$`, 'i') })

    res.json(transcript)
  } catch (error) {
    res.json(error.message)
  }
}

const deleteStudent = async (req, res) => {
  try {
    const { className, studentId } = req.params

    const response = await Transcript.updateOne(
      { className: new RegExp(`^${className}$`, 'i') },
      { $pull: { studentGrades: { studentId: { $eq: studentId }}}}
    )

    res.json(response)
  } catch (error) {
    res.json(error.message)
  }
}
module.exports = {
  getAllTranscripts, getTranscript, createTranscript, updateTranscript, deleteTranscript, deleteStudent
}