import { client } from './client'

const producer = async () => {
    await client.connect()

    await client.publish('channel-1', "From producer: hello !")
}

producer()

