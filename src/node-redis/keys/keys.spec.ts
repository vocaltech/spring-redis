import { redis } from "../config/redis"

describe('test redis keys ', () => { 
    beforeAll(async () => {
        await redis.connect()
    })

    afterAll(() => {
        redis.flushAll()
        redis.quit()
    })

    it(' should store some values', async () => {
        redis.set("key1", "val1")
        redis.set("key2", "val2")
        
        const { keys } = await redis.scan(0)

        expect(keys.length).toBe(2)
    })
})