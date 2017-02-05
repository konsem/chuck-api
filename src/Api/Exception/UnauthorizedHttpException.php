<?php

/**
 * UnauthorizedHttpException.php - created 2 Oct 2016 13:19:43
 *
 * @copyright Copyright (c) Mathias Schilling <m@matchilling>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 *
 */
namespace Chuck\App\Api\Exception;

/**
 *
 * UnauthorizedHttpException
 *
 * @package Chuck\App\Api
 *
 */
class UnauthorizedHttpException extends \Symfony\Component\HttpKernel\Exception\HttpException
{

    /**
     * @param string     $message
     * @param \Exception $previous The previous exception
     * @param int        $code     The internal exception code
     */
    public function __construct($message, \Exception $previous = null, $code = 0)
    {
        parent::__construct(401, $message, $previous, [], $code);
    }
}
