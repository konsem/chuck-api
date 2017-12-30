<?php
/**
 * TheDailyChuckController.php - created Fri 29 Dec 11:50:14 GMT 2017
 *
 * @copyright Copyright (c) Mathias Schilling <m@matchilling>
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 *
 */
namespace Chuck\App\Api\Controller\Feed;

use \Symfony\Component\HttpFoundation;
use \DOMDocument;
use Symfony\Component\HttpKernel\Exception;
use Chuck\App\Api\Model;

/**
 * TheDailyChuckController
 *
 * @package Chuck\App\Api
 */
class TheDailyChuckController
{

    /**
     *
     * @var string
     */
    const DESCRIPTION = 'Get your daily dose of the best #ChuckNorrisFacts every morning straight into your inbox.';

    /**
     *
     * @var string
     */
    const TITLE = 'The Daily Chuck';

    /**
     *
     * @param \Silex\Application $app
     * @param HttpFoundation\Request $request
     * @param string $extension
     * @throws Exception\NotFoundHttpException
     * @return \Chuck\App\Api\Model\JsonResponse|\Symfony\Component\HttpFoundation\Response
     */
    public function indexAction(\Silex\Application $app, HttpFoundation\Request $request, string $extension)
    {
        switch ($extension) {
            case 'json':
                return $this->jsonAction($app);
            case 'xml':
                return $this->xmlAction($app);
            default:
                throw new Exception\NotFoundHttpException();
        }
    }

    /**
     *
     * @param \Silex\Application $app
     * @return \Chuck\App\Api\Model\JsonResponse
     */
    private function jsonAction(\Silex\Application $app)
    {
        $response = [
            'title' => self::TITLE,
            'description' => self::DESCRIPTION,
            'link' => $app['url_generator']->generate('api', [], \Symfony\Component\Routing\Generator\UrlGenerator::ABSOLUTE_URL),
            'items' => []
        ];

        $issueList = $app['service.the_daily_chuck']->getIssues();
        foreach ($issueList as $issueElement) {
            /** @var \Chuck\Entity\Joke $joke */
            $joke = $issueElement['joke'];

            array_push($response['items'], [
                'title' => $issueElement['issue'],
                'content' => $joke->getValue(),
                'link' => $app['url_generator']->generate('api.get_joke', [
                    'id' => $joke->getId()
                ], \Symfony\Component\Routing\Generator\UrlGenerator::ABSOLUTE_URL),
                'published' => $issueElement['published']
            ]);
        }

        return new Model\JsonResponse($response);
    }

    /**
     *
     * @param \Silex\Application $app
     * @return \Symfony\Component\HttpFoundation\Response
     */
    private function xmlAction(\Silex\Application $app)
    {
        $rss = new DOMDocument('1.0', 'UTF-8');
        $rss->formatOutput = true;

        $rootNode = $rss->createElement('rss');
        $rss->appendChild($rootNode);

        $rssVersion = $rss->createAttribute('version');
        $rssVersion->appendChild($rss->createTextNode('2.0'));
        $rootNode->appendChild($rssVersion);

        $channel = $rss->createElement('channel');
        $rootNode->appendChild($channel);

        $title = $rss->createElement('title');
        $title->appendChild($rss->createTextNode(self::TITLE));
        $channel->appendChild($title);

        $description = $rss->createElement('description');
        $description->appendChild($rss->createTextNode(self::DESCRIPTION));
        $channel->appendChild($description);

        $link = $rss->createElement('link');
        $link->appendChild($rss->createTextNode($app['url_generator']->generate('api', [], \Symfony\Component\Routing\Generator\UrlGenerator::ABSOLUTE_URL)));
        $channel->appendChild($link);

        $issueList = $app['service.the_daily_chuck']->getIssues();
        foreach ($issueList as $issueElement) {
            /** @var \Chuck\Entity\Joke $joke */
            $joke = $issueElement['joke'];

            $item = $rss->createElement('item');
            $channel->appendChild($item);

            $itemTitle = $rss->createElement('title');
            $itemTitle->appendChild($rss->createTextNode($issueElement['issue']));
            $item->appendChild($itemTitle);

            $itemContent = $rss->createElementNS('http://purl.org/rss/1.0/modules/content/', 'content:encoded');
            $itemContent->appendChild($rss->createTextNode($joke->getValue()));
            $item->appendChild($itemContent);

            $itemDescription = $rss->createElement('description');
            $itemDescription->appendChild($rss->createTextNode($joke->getValue()));
            $item->appendChild($itemDescription);

            $itemLink = $rss->createElement('link');
            $itemLink->appendChild($rss->createTextNode($app['url_generator']->generate('api.get_joke', [
                'id' => $joke->getId()
            ], \Symfony\Component\Routing\Generator\UrlGenerator::ABSOLUTE_URL)));
            $item->appendChild($itemLink);

            $itemPublished = $rss->createElement('pubDate');
            $itemPublished->appendChild($rss->createTextNode($issueElement['published']));
            $item->appendChild($itemPublished);
        }

        return new HttpFoundation\Response($rss->saveXML(), 200, [
            'content-type' => 'application/xml'
        ]);
    }
}
