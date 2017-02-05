<?php

/**
 * AlexaController.php - created 2 Oct 2016 19:45:17
 *
 * @copyright Copyright (c) Mathias Schilling <m@matchilling>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 *
 */
namespace Chuck\App\Api\Controller\Integration;

use Chuck\Entity;
use \Symfony\Component\HttpKernel\Exception as Exception;
use \Symfony\Component\HttpFoundation as HttpFoundation;

/**
 *
 * AlexaController
 *
 * @package Chuck\App\Api
 *
 */
class AlexaController
{

    /**
     *
     * @param  \Silex\Application     $app
     * @param  HttpFoundation\Request $request
     * @throws Exception\NotFoundHttpException
     * @return HttpFoundation\Response
     */
    public function indexAction(\Silex\Application $app, HttpFoundation\Request $request)
    {
        /* @var \Psr\Log\LoggerInterface $logger */
        $logger = $app['monolog'];
        
        $logger->info(
            'Incoming request data',
            $data = $request->request->all()
        );
        
        $response = $app['chuck.joke']->random($category = null, $parentalControl = true);
        if (! $response instanceof Entity\Joke) {
            throw new Exception\NotFoundHttpException();
        }
        
        return new HttpFoundation\JsonResponse([
            'version'  => '1.0',
            'response' => [
                'outputSpeech' => [
                    'type'    => 'PlainText',
                    'text'    => $response->getValue(),
                ],
                'shouldEndSession' => true,
            ],
        ]);
    }
}
