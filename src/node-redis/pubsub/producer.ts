import { redis } from '../config/redis'

const producer = async () => {
    await redis.connect()

    await redis.publish('channel-1', "From producer: hello !")
}

producer()

