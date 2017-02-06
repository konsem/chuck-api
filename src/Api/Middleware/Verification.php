<?php

/**
 * Verification.php - created 2 Oct 2016 10:55:09
 *
 * @copyright Copyright (c) Mathias Schilling <m@matchilling>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 *
 */
namespace Chuck\App\Api\Middleware;

use Chuck\App\Api\Exception;
use \Symfony\Component\HttpFoundation;

/**
 *
 * Verification
 *
 * @package Chuck\App\Api
 *
 */
class Verification
{
    /**
     * 
     * @param  HttpFoundation\Request $request
     * @param  \Silex\Application $app
     * @throws Exception\UnauthorizedHttpException
     */
    public static function alexaOrigin(HttpFoundation\Request $request, \Silex\Application $app)
    {
        $data = $request->request->all();

        if (empty($data['session']['application']['applicationId'])) {
            throw new Exception\UnauthorizedHttpException('Could not verify "ALEXA_SKILL_ID".');
        }
        
        if ($data['session']['application']['applicationId'] !== $app['config']['alexa_skill_id']) {
            throw new Exception\UnauthorizedHttpException('Could not verify "ALEXA_SKILL_ID".');
        }
    }

    /**
     * 
     * @param  HttpFoundation\Request $request
     * @param  \Silex\Application $app
     * @throws Exception\UnauthorizedHttpException
     */
    public static function slackOrigin(HttpFoundation\Request $request, \Silex\Application $app)
    {
        $token = $request->request->get(
            'token',
            $request->query->get('token', null)
        );

        if ($token !== $app['config']['slack_verification_token']) {
            throw new Exception\UnauthorizedHttpException('Could not verify "SLACK_VERIFICATION_TOKEN".');
        }
    }
}
