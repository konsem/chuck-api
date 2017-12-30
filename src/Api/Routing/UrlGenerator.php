<?php
/**
 * UrlGenerator.php - created Sat 30 Dec 14:25:36 GMT 2017
 *
 * @copyright Copyright (c) Mathias Schilling <m@matchilling>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 *
 */
namespace Chuck\App\Api\Routing;

/**
 * UrlGenerator
 *
 * @package Chuck\App\Api
 */
class UrlGenerator extends \Symfony\Component\Routing\Generator\UrlGenerator
{

    /**
     *
     * {@inheritdoc}
     * @see \Symfony\Component\Routing\Generator\UrlGenerator::generate()
     */
    public function generate($name, $parameters = array(), $referenceType = self::ABSOLUTE_PATH)
    {
        return str_replace('http://', 'https://', parent::generate($name, $parameters, $referenceType));
    }
}