const mongoose = require('mongoose')

const transcriptSchema = new mongoose.Schema({
  user: {
    type: mongoose.Schema.ObjectId,
    ref: 'User',
    required: [true, "require userId"]
  },
  className: {
    type: String, 
    required: true
  },
  studentGrades: [
    {
      name: {
        type: String,
      },
      studentId: {
        type: Number,
      },
      grade: {
        type: Number,
      },
      _id: false
    }
  ],
  JsonSignature: {
    type: String,
    default: ""
  },
  PdfSignature: {
    type: String, 
    default: ""
  },
  isSignedJson: {
    type: Boolean
  },
  isSignedPdf: {
    type: Boolean
  },
  __v: {
    type: Number,
    select: false // exclude this field from query
  }
})

// using compound index to ensure unique className among users
transcriptSchema.index({ user: 1, className: 1 }, { unique: true });

transcriptSchema.pre('save', function (next) {
  let {className} = this

  className = className.trim(' ').toLowerCase()
  className = className[0].toUpperCase() + className.slice(1)

  this.className = className
  console.log(className)
  next()
})

module.exports = mongoose.model('Transcript', transcriptSchema)