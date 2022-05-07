import { redis } from "../config/redis"
import { Position } from "../models/position.interface"

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

    describe('queue simulation with Position', () => {
        const userId = "5f2697cc69b07f2ff81fc279"
        const trackId = "61ae78e46beff9e8495a8d45"

        it(' should enqueue some positions', async() => {
            let pos: Position = {
                user_id: new Object(userId),
                track_id: new Object(trackId),
                coordinates: [1.52, 47.2],
                timestamp: new Date()
            }

            await redis.lPush(userId, JSON.stringify(pos))

            pos = {
                user_id: new Object(userId),
                track_id: new Object(trackId),
                coordinates: [1.53, 47.3],
                timestamp: new Date()
            }

            await redis.lPush(userId, JSON.stringify(pos))

            const queueContent = await redis.lRange(userId, 0, -1)
            let count: number = 0

            queueContent.map(posStr => {
                const pos: Position = JSON.parse(posStr)

                switch (count) {
                    case 0:
                        expect(pos.coordinates).toEqual([1.53, 47.3])
                        break;

                    case 1:
                        expect(pos.coordinates).toEqual([1.52, 47.2])
                        break;
            
                    default:
                        break;
                }

                count++
            })
        })

        it(' should dequeue one Position', async () => { 
            const popElt = await redis.rPop(userId) as string
            const dequeuePos: Position = JSON.parse(popElt)

            expect(dequeuePos.coordinates).toEqual([1.52, 47.2])
        })
    })

    describe('stack simulation', () => { 

    })
})