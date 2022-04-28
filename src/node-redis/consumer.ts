import { client } from "./client";

const consumer = async () => {

    const subscriber = client.duplicate()
    await subscriber.connect()

    await subscriber.subscribe('channel-1', (msg) => {
        console.log(msg)
    })
}

consumer()