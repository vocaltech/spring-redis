import { Message, Producer } from 'redis-smq'

const message = new Message()

message
    .setBody({ message: 'Hello Redis !'})
    .setTTL(3600000) // in ms
    .setQueue('test_queue')

const producer = new Producer()
producer.produce(message, err => {
    if (err) {
        console.log(err)
    } else {
        const msgId = message.getId()
        console.log(`successfully produced msg - id: ${msgId}`)
    }
})