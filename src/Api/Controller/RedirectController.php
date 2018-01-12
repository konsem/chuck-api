<?php

/**
 * RedirectController.php - created 12 Jan 2018 01:29:09
 *
 * @copyright Copyright (c) Mathias Schilling <m@matchilling>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 */
namespace Chuck\App\Api\Controller;

use Silex\Application as App;
use Symfony\Component\HttpFoundation;

/**
 * RedirectController
 *
 * @package Chuck\App\Api
 */
class RedirectController
{

    /**
     *
     * @param App $app
     * @param HttpFoundation\Request $request
     * @return \Symfony\Component\HttpFoundation\RedirectResponse
     */
    public function urlRedirectAction(App $app, HttpFoundation\Request $request)
    {
        $params = $request->get('_route_params');

        return $app->redirect($params['path'], $params['permanent'] ? 301 : 302);
    }
}
