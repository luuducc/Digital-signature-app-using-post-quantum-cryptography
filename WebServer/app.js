const mongoose = require('mongoose')
const express = require('express')
const app = express()
const transcripts = require('./routes/transcripts')
const authentications = require('./routes/authentications')
const publicKey = require('./routes/publicKey')
const verify = require('./routes/verify')
require('dotenv').config()

const port = 5000

// Add middleware to set CORS headers
app.use((req, res, next) => {
  res.setHeader('Access-Control-Allow-Origin', '*'); // Allow requests from any origin
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, PATCH, DELETE'); // Allow specific HTTP methods
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization'); // Allow specific headers
  next();
});

app.use(express.static('./public'))
app.use(express.json())

app.use('/api/transcript', transcripts)
app.use('/api/auth', authentications)
app.use('/api/key/register', publicKey)
app.use('/api/verify', verify)

app.get('/', (req, res) => {
  res.json('hello')
})

const start = async () => {
  try {
    await mongoose.connect(process.env.MONGO_URI)
    app.listen(port, () => {
      console.log(`server is listening on http://localhost:${port}`)
    })
  } catch(err) {
    console.log(err)
  }
}

start()

