const Transcript = require('../models/Transcript')

const getAllTranscripts = async (req, res) => {
  try {
    const { userId } = req.params
    if( !userId ) {
      throw new Error('Invalid userId')
    }
    const transcripts = await Transcript.find({ user: userId })
    if(transcripts) res.json(transcripts)
    else throw new Error('cannot find')
  } catch (error) {
    res.json(error.message)
    console.log(error.message)
  }
}

const getTranscript = async (req, res) => {
  try {
    const {className} = req.params
    const transcript = await Transcript.findOne()
      .where({ className: new RegExp(`^${className}$`, 'i')})
      .populate({ path: 'user' })

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
    const newTranscript = await Transcript.create(req.body )
    res.json(newTranscript)
  } catch (error) {
    res.json(error.message)
  }
}

const updateTranscript = async (req, res) => {
  try {
    const { className } = req.params
    const studentGrades = req.body

    const transcript = await Transcript.findOneAndUpdate(
      { className: new RegExp(`^${className}$`, 'i') },
      { studentGrades },
      { new: true }
    )

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