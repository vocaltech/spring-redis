import { createClient } from 'redis'

require('dotenv-safe').config();

export const redis = createClient({
  url: process.env.REDIS_TLS_URL,
  socket: {
    tls: true,
    rejectUnauthorized: false
  }
})