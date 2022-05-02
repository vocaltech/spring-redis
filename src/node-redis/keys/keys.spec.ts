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

    it(' should get some values', async () => {
        let v = await redis.get("key1")
        expect(v).toBe("val1")

        v = await redis.get("key2")
        expect(v).toBe("val2")
    })
})