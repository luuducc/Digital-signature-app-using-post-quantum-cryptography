const Transcript = require('../models/Transcript')

const getAllTranscripts = async (userId) => {
  const transcripts = await Transcript.find({ user: userId })
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
module.exports = {
  getTranscript, getAllTranscripts, createTranscript
}