import { redis } from "././config/redis";

const consumer = async () => {

    const subscriber = redis.duplicate()
    await subscriber.connect()

    await subscriber.subscribe('channel-1', (msg) => {
        console.log(msg)
    })
}

consumer()