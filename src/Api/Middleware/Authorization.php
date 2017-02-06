<?php

/**
 * Verification.php - created 2 Oct 2016 14:20:00
 *
 * @copyright Copyright (c) Mathias Schilling <m@matchilling>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 *
 */
namespace Chuck\App\Api\Middleware;

use Chuck\App\Api\Exception;
use \Symfony\Component\HttpFoundation as HttpFoundation;

/**
 *
 * Authorization
 *
 * @package Chuck\App\Api
 *
 */
class Authorization
{
    /**
     * Determine whether someone is allowed to write
     * 
     * @param  HttpFoundation\Request $request
     * @param  \Silex\Application     $app
     * @throws Exception\UnauthorizedHttpException
     */
    public static function write(HttpFoundation\Request $request, \Silex\Application $app)
    {
        /* @var HttpFoundation\HeaderBag $headers */
        $headers = $request->headers;

        // Extract the bearer token
        $apiKey = $headers->has('authorization')
            ? substr($headers->get('authorization'), 7)
            : null;

        if (! in_array($apiKey, $app['acl']['write'])) {
            throw new Exception\UnauthorizedHttpException('Could not verify "API key".');
        }
    }
}
