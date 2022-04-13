import { Consumer, Message } from 'redis-smq' 

const consumer = new Consumer()

const messageHandler = (msg: Message , cb: any) => {
    const payload = msg.getBody();
    console.log('Message payload', payload);
    cb(); // acknowledging the message
}

// The second parameter is for enabling priority queuing for the message handler
// If you are willing to consume messages with priority, do not forget to set a priority for your messages
// See Reliable Priority Queues for more details
consumer.consume('test_queue', false, messageHandler, (err, isRunning) => {
    if (err) console.error(err);
    // the message handler will be started only if the consumer is running
    else console.log(`Message handler has been registered. Running status: ${isRunning}`); // isRunning === false
});

consumer.run()