const { exec } = require('child_process')
const { error } = require('console')
const fs = require('fs')
const path = require('path')
const { stdout, stderr } = require('process')

const params = fs.readFileSync('params.txt', 'utf-8')

exec(`java -jar verify.jar ${params}`, (error, stdout, stderr) => {
  if (error) {
    console.error(`failed to execute jar file: ${error.message}`)
    return
  }
  if (stderr) {
    console.error(`java program stderr: ${stderr}`)
    return
  }

  const result = stdout.trim()
  console.log(result)
})