<?php
/**
 * JokesController.php - created Mar 6, 2016 3:03:18 PM
 *
 * @copyright Copyright (c) Mathias Schilling <m@matchilling>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 *
 */
namespace Chuck\App\Api\Controller;

use \Chuck\App\Api\Service\CacheService as CacheService;
use \Chuck\App\Api\Formatter as Formatter;
use \Chuck\App\Api\Model as Model;
use \Chuck\Entity as Entity;
use \Symfony\Component\HttpFoundation as HttpFoundation;
use \Symfony\Component\HttpKernel\Exception as Exception;

/**
 *
 * JokesController
 *
 * @package Chuck\App\Api
 *
 */
class JokesController
{
    use \Chuck\Util\LoggerTrait;

    /**
     *
     * @param  \Silex\Application $app
     * @param  HttpFoundation\Request $request
     * @throws Exception\NotFoundHttpException
     * @return HttpFoundation\Response|Model\JsonResponse
     */
    public function categoryAction(\Silex\Application $app, HttpFoundation\Request $request)
    {
        /* @var \Chuck\JokeFacade $jokeFacade */
        $jokeFacade =  $app['chuck.joke'];

        /* @var array $categories */
        $categories = array_map(function ($category) {
            return $category['name'];
        }, $jokeFacade->getCategories());

        if ('text/plain' === $request->headers->get('accept')) {
            return new HttpFoundation\Response(
                implode("\n", $categories),
                HttpFoundation\Response::HTTP_OK,
                [ 'content-type' => 'text/plain' ]
            );
        }

        return new Model\JsonResponse($categories);
    }

    /**
     *
     * @param  \Silex\Application     $app
     * @param  HttpFoundation\Request $request
     * @return Model\JsonResponse
     */
    public function createAction(\Silex\Application $app, HttpFoundation\Request $request)
    {
        /* @var \Chuck\JokeFacade $jokeFacade */
        $jokeFacade = $app['chuck.joke'];

        /* @var Entity\Factory $entityFactory */
        $entityFactory = $app['chuck.entity_factory'];

        /* @var Entity\Joke $joke */
        $joke = $entityFactory->fromArray(
            Entity\Joke::class,
            [
                'id'         => \Chuck\Util::createSlugUuid(),
                'categories' => $request->request->get('categories', []),
                'value'      => $request->request->get('value')
            ]
        );

        /* @var Formatter\JokeFormatter $jokeFormatter */
        $jokeFormatter = new Formatter\JokeFormatter($app['url_generator']);

        return new Model\JsonResponse(
            $jokeFormatter->format($jokeFacade->insert($joke))
        );
    }

    /**
     *
     * @param  \Silex\Application     $app
     * @param  HttpFoundation\Request $request
     * @param  string                 $id
     * @throws Exception\NotFoundHttpException
     * @return string|Model\JsonResponse
     */
    public function getAction(\Silex\Application $app, HttpFoundation\Request $request, $id)
    {
        if ('application/json' === $request->headers->get('accept')) {
            /** @var CacheService $cache */
            $cacheService = $app['cache_service'];
            
            /* @var Entity\Joke $joke */
            $joke = $cacheService->getJokeById($id);

            /* @var Formatter\JokeFormatter $jokeFormatter */
            $jokeFormatter = new Formatter\JokeFormatter($app['url_generator']);

            return new Model\JsonResponse(
                $jokeFormatter->format($joke)
            );
        }
        
        /* @var \Chuck\JokeFacade $jokeFacade */
        $jokeFacade = $app['chuck.joke'];
        $jokeWindow = $jokeFacade->window($id);

        if (! $jokeWindow instanceof Entity\JokeWindow) {
            throw new Exception\NotFoundHttpException();
        }

        /* @var Generator\UrlGenerator $urlGenerator */
        $urlGenerator = $app['url_generator'];

        return $app['twig']->render('joke.html', [
            'page_title'    => trim(substr($jokeWindow->getCurrent()->getValue(), 0, 120), '.') . ' ...',
            'current_joke'  => [
                'id'    => $jokeWindow->getCurrent()->getId(),
                'title' => trim(substr($jokeWindow->getCurrent()->getValue(), 0, 120), '.') . ' ...',
                'url'   => $urlGenerator->generate('api.get_joke', ['id' => $id]),
                'value' => $jokeWindow->getCurrent()->getValue()
            ],
            'next_joke'     => [
                'id'    => $next = $jokeWindow->getNext()->getId(),
                'title' => trim(substr($jokeWindow->getNext()->getValue(), 0, 120), '.') . ' ...',
                'url'   => $urlGenerator->generate('api.get_joke', ['id' => $next]),
                'value' => $jokeWindow->getNext()->getValue()
            ],
            'previous_joke' => [
                'id'    => $prev = $jokeWindow->getPrevious()->getId(),
                'title' => trim(substr($jokeWindow->getPrevious()->getValue(), 0, 120), '.') . ' ...',
                'url'   => $urlGenerator->generate('api.get_joke', ['id' => $prev]),
                'value' => $jokeWindow->getPrevious()->getValue()
            ]
        ]);
    }

    /**
     *
     * @param  \Silex\Application $app
     * @param  HttpFoundation\Request $request
     * @throws Exception\NotFoundHttpException
     * @return HttpFoundation\Response|Model\JsonResponse
     */
    public function randomAction(\Silex\Application $app, HttpFoundation\Request $request)
    {
        $category = $request->query->get('category', null);

        $joke = $category != null
            ? $app['chuck.joke']->random($category)
            : $app['cache_service']->getRandomJoke();

        if (! $joke instanceof Entity\Joke) {
            throw new Exception\NotFoundHttpException();
        }

        if ('text/plain' === $request->headers->get('accept')) {
            return new HttpFoundation\Response(
                $joke->getValue(),
                HttpFoundation\Response::HTTP_OK,
                [ 'content-type' => 'text/plain' ]
            );
        }

        /* @var Formatter\JokeFormatter $jokeFormatter */
        $jokeFormatter = new Formatter\JokeFormatter($app['url_generator']);

        return new Model\JsonResponse(
            $jokeFormatter->format($joke)
        );
    }

    /**
     *
     * @param  \Silex\Application $app
     * @param  HttpFoundation\Request $request
     * @throws Exception\NotFoundHttpException
     * @return HttpFoundation\Response|Model\JsonResponse
     */
    public function searchAction(\Silex\Application $app, HttpFoundation\Request $request)
    {
        if (! $query = $request->query->get('query', null)) {
            throw new Exception\NotFoundHttpException();
        }

        $response = $app['chuck.joke']->searchByQuery($query);

        if ('text/plain' === $request->headers->get('accept')) {
            $jokeList = array_map(
                function (Entity\Joke $joke) {
                    return $joke->getValue();
                },
                $response['result']
            );

            return new HttpFoundation\Response(
                implode("\n", $jokeList),
                HttpFoundation\Response::HTTP_OK,
                [
                    'content-type'   => 'text/plain',
                    'x-result-count' => $response['total']
                ]
            );
        }

        /* @var Formatter\JokeFormatter $jokeFormatter */
        $jokeFormatter = new Formatter\JokeFormatter($app['url_generator']);

        foreach ($response['result'] as &$joke) {
            $joke = $jokeFormatter->format($joke);
        }

        return new Model\JsonResponse(
            $response,
            Model\JsonResponse::HTTP_OK,
            [ 'x-result-count' => $response['total'] ]
        );
    }

    /**
     *
     * @param  \Silex\Application     $app
     * @param  HttpFoundation\Request $request
     * @param  string                 $id
     * @throws Exception\UnprocessableEntityHttpException
     * @return Model\JsonResponse
     */
    public function updateAction(\Silex\Application $app, HttpFoundation\Request $request, $id)
    {
        /* @var \Chuck\JokeFacade $jokeFacade */
        $jokeFacade = $app['chuck.joke'];

        /* @var Entity\Joke $joke */
        $joke = $jokeFacade->get($id);

        if (! $joke instanceof Entity\Joke) {
            throw new Exception\UnprocessableEntityHttpException();
        }

        /* @var Entity\Factory $entityFactory */
        $entityFactory = $app['chuck.entity_factory'];

        /* @var Entity\Joke $joke */
        $joke = $entityFactory->fromArray(
            Entity\Joke::class,
            [
                'id'         => $joke->getId(),
                'categories' => $request->request->get('categories', $joke->getCategories()),
                'value'      => $request->request->get('value', $joke->getValue())
            ]
        );
        $joke = $jokeFacade->update($joke);
        
        $app['cache']->invalidateJokeCache($id);
        
        /* @var Formatter\JokeFormatter $jokeFormatter */
        $jokeFormatter = new Formatter\JokeFormatter($app['url_generator']);
        
        return new Model\JsonResponse(
            $jokeFormatter->format($joke)
        );
    }
}
