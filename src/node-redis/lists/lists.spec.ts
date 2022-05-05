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

            await redis.lPush(queueId, "a")
            await redis.lPush(queueId, "b")
            await redis.lPush(queueId, "c")

            const queueContent = await redis.lRange(queueId, 0, -1)

            expect(queueContent).toEqual(['c', 'b', 'a'])
        })

        it(' should dequeue some datas', async () => { 
            const queueId: string = 'user:1000' 
            let count = 0;

            while (await redis.lLen(queueId) > 0) {
                let popElt = await redis.rPop(queueId)

                const queueContent = await redis.lRange(queueId, 0, -1)

                switch (count) {
                    case 0:
                        expect(queueContent).toEqual(['c', 'b'])
                        break;

                    case 1:
                        expect(queueContent).toEqual(['c'])
                        break;

                    case 2:
                        expect(queueContent).toEqual([])
                        break;
                }

                count++
            }
        })
    })
})