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

    it(' should delete some value', async () => {
        let delResult = await redis.del("key1");
        expect(delResult).toBe(1)

        let v = await redis.get("key1")
        expect(v).toBeNull()
    })

    it(' should return a JSON object', async () => {
        redis.set("user:1000", "{ \"name\": \"joe\", \"position\": [1.0, 45.8] }")

        let res = await redis.get("user:1000") as string
        let json = JSON.parse(res)

        expect(json.name).toBe("joe")
        expect(json.position[0]).toBe(1.0)
        expect(json.position[1]).toBe(45.8)
    })
})