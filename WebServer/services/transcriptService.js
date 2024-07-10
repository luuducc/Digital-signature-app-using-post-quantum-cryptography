const Transcript = require('../models/Transcript')

const getAllTranscripts = async () => {
  // for admin only
  // const transcripts = await Transcript.find({ user: userId })
  const transcripts = await Transcript.find()
  return transcripts
}

const getTranscript = async (className, userId) => {
  try {
    const transcript = await Transcript.findOne()
      .where({ 
        className: new RegExp(`^${className}$`, 'i'),
        user: userId
      })
      .populate({ path: 'user' })

    return transcript
  } catch (error) {
    res.json(error.message)
  }
}

const createTranscript = async (transcript, userId) => {
  const newTranscript = await Transcript.create({  user: userId, ...transcript })
  return newTranscript
}

const updateTranscript = async (className, studentGrades, userId) => {
  const transcript = await Transcript.findOneAndUpdate(
    { 
      className: new RegExp(`^${className}$`, 'i'),
      user: userId
    },
    { studentGrades },
    { new: true }
  )
  return transcript
}
module.exports = {
  getTranscript, getAllTranscripts, createTranscript, updateTranscript
}