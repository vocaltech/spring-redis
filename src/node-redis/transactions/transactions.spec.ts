import { redis } from "../config/redis"

describe('transactions testing', () => { 
    beforeAll(async () => {
        await redis.connect()
    })

    afterAll(() => {
        redis.flushAll()
        redis.quit()
    })

    it(' should increment 2 vars', async () => {
        redis.set('a', 2)

        const [a, b] = await redis.multi()
            .incr('a')
            .incr('b')
            .exec()

        expect(await redis.get('a')).toBe('3')
        expect(await redis.get('b')).toBe('1')
    })
})