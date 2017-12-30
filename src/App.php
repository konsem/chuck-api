<?php

/**
 * App.php - created Mar 6, 2016 3:03:18 PM
 *
 * @copyright Copyright (c) Mathias Schilling <m@matchilling>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 *
 */
require_once sprintf('%s/vendor/autoload.php', $appDirectory = dirname(__DIR__));

if (file_exists(sprintf('%s/.env', $appDirectory))) {
    $dotenv = new \Dotenv\Dotenv($appDirectory);
    $dotenv->load();
}

$app = new \Silex\Application();

$app['application_env'] = \Chuck\Util::getEnvOrDefault('APPLICATION_ENV', 'production');
$app['debug'] = 'production' === $app['application_env'] ? false : true;

$app->extend('routes', function (\Symfony\Component\Routing\RouteCollection $routes, \Silex\Application $app) {
    $loader = new \Symfony\Component\Routing\Loader\YamlFileLoader(new \Symfony\Component\Config\FileLocator(__DIR__ . '/../config'));

    $collection = $loader->load('routes.yml');
    $routes->addCollection($collection);

    return $routes;
});

$app->register(new \Chuck\App\Api\ServicesLoader());

$streamHandler = new \Monolog\Handler\StreamHandler('php://stdout', \Monolog\Logger::INFO);
$streamHandler->setFormatter(new \Bramus\Monolog\Formatter\ColoredLineFormatter(new \Bramus\Monolog\Formatter\ColorSchemes\TrafficLight()));
$app->register(new \Silex\Provider\MonologServiceProvider(), [
    'monolog.name' => 'chuck_norris',
    'monolog.handler' => $streamHandler
]);

$app->register(new \Silex\Provider\TwigServiceProvider(), [
    'twig.path' => __DIR__ . '/../assets/views/'
]);
$app['twig'] = $app->share($app->extend('twig', function ($twig, $app) {
    $twig->addFunction(new \Twig_SimpleFunction('asset', function ($asset) use ($app) {
        if ($app['debug']) {
            return sprintf('/%s', ltrim($asset, '/'));
        } else {
            return sprintf('https://assets.chucknorris.host/%s', ltrim($asset, '/'));
        }
    }));
    $twig->addExtension(new \nochso\HtmlCompressTwig\Extension());
    return $twig;
}));

$app->error(function (\Exception $exception, $httpStatusCode) use ($app) {
    if ($app['debug']) {
        return;
    }

    if (\Symfony\Component\HttpFoundation\Response::HTTP_NOT_FOUND === $httpStatusCode) {
        return $app['twig']->render('error_404.html');
    }

    return $app->json([
        'message' => 'Whoops, looks like something went wrong.'
    ]);
});

return $app;
