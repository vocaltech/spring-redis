import { redis } from "../config/redis"

describe('lists tests', () => { 
    beforeAll(async () => {
        await redis.connect()
    })

    afterAll(() => {
        redis.flushAll()
        redis.quit()
    })

    describe('queue simulation', () => { 
        it(' should enqueue some datas', async () => { 
            const queueId: string = 'user:1000' 

            redis.lPush(queueId, "a")
            redis.lPush(queueId, "b")
            redis.lPush(queueId, "c")

            const queueContent = await redis.lRange(queueId, 0, -1)

            console.log(queueContent)
        })
    })
})