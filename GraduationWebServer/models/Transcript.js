const mongoose = require('mongoose')

const transriptSchema = new mongoose.Schema({
  user: {
    type: mongoose.Schema.ObjectId,
    ref: 'User',
    required: [true, "require userId"]
  },
  className: {
    type: String, 
    unique: true
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
  __v: {
    type: Number,
    select: false // exclude this field from query
  }
})

transriptSchema.pre('save', function (next) {
  let {className} = this

  className = className.trim(' ').toLowerCase()
  className = className[0].toUpperCase() + className.slice(1)

  this.className = className
  console.log(className)
  next()
})

module.exports = mongoose.model('Transcript', transriptSchema)