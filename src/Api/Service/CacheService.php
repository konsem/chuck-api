<?php

/**
 * CacheService.php - created  22 Apr 2017 13:23:40
 *
 * @copyright Copyright (c) Mathias Schilling <m@matchilling>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 *
 */
namespace Chuck\App\Api\Service;

use Chuck\Entity;
use Chuck\JokeFacade;
use Predis\ClientInterface;

/**
 *
 * CacheService
 *
 * @package \Chuck\App\Api
 *
 */
class CacheService
{
    /**
     * @var ClientInterface
     */
    private $cache;
    
    /** @var string[] */
    private static $cacheKeys = [
        Entity\Joke::class => 'joke_%s',
    ];

    /**
     * @var Entity\Factory
     */
    private $entityFactory;

    /**
     * @var JokeFacade
     */
    private $jokeFacade;

    /**
     * @param ClientInterface $cache
     * @param Entity\Factory $entityFactory
     * @param JokeFacade $jokeFacade
     */
    public function __construct(ClientInterface $cache, Entity\Factory $entityFactory, JokeFacade $jokeFacade)
    {
        $this->cache = $cache;
        $this->entityFactory = $entityFactory;
        $this->jokeFacade = $jokeFacade;
    }

    /**
     * @param  string $id
     * @return Entity\Joke
     */
    public function getJokeById($id): Entity\Joke
    {
        $cacheKey = sprintf(self::$cacheKeys[Entity\Joke::class], $id);
        $response = $this->cache->get($cacheKey);
        
        if (null === $response) {
            $joke = $this->jokeFacade->get($id);
            $json = $this->entityFactory->toJson($joke);
            
            $this->cache->set($cacheKey, $json);
            
            return $joke;
        }
        
        return $this->entityFactory->fromJson(Entity\Joke::class, $response);
    }

    /**
     * @return Entity\Joke
     */
    public function getRandomJoke(): Entity\Joke
    {
        $randomKey = $this->cache->randomkey();

        if (null === $randomKey) {
            $joke = $this->jokeFacade->random();
            $key  = sprintf(self::$cacheKeys[Entity\Joke::class], $joke->getId());
            $json = $this->entityFactory->toJson($joke);
            
            $this->cache->set($key, $json);
            
            return $joke;
        }
        
        $response = $this->cache->get($randomKey);
        return $this->entityFactory->fromJson(Entity\Joke::class, $response);
    }
    
    /**
     * @param string $id
     * @return void
     */
    public function invalidateJokeCache($id)
    {
        $response = $this->cache->del([
            sprintf(self::$cacheKeys[Entity\Joke::class], $id)
        ]);
    }
}
