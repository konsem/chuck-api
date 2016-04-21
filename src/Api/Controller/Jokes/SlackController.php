<?php

/**
 * SlackController.php - created Mar 6, 2016 3:03:18 PM
 *
 * @copyright Copyright (c) pinkbigmacmedia
 *
 */
namespace Chuck\App\Api\Controller\Jokes;

/**
 *
 * SlackController
 *
 * @package Chuck\App\Api
 *
 */
class SlackController
{
    use \Chuck\Util\LoggerTrait;

    /**
     *
     * @var string
     */
    protected static $iconUrl = 'https://assets.chucknorris.host/img/avatar/chuck-norris.png';

    /**
     * The holy shrug
     *
     * @var string
     */
    protected static $shrug = '¯\_(ツ)_/¯';

    /**
     *
     * @param  \Chuck\App\Api\Model\SlackInput           $input
     * @param  \Chuck\JokeFacade                         $jokeFacade
     * @param  \Symfony\Component\HttpFoundation\Request $request
     * @return \Symfony\Component\HttpFoundation\JsonResponse
     */
    protected function doAddCommand(
        \Chuck\App\Api\Model\SlackInput           $input,
        \Chuck\JokeFacade                         $jokeFacade,
        \Symfony\Component\HttpFoundation\Request $request
    ) {

        if (! $value = $input->getInputWithoutArgs()) {
            return new \Symfony\Component\HttpFoundation\JsonResponse(
                [
                    'icon_url'      => self::$iconUrl,
                    'text'          => sprintf('Invalid input given ("%s").', $input->getInputWithoutArgs()),
                    'mrkdwn'        => true
                ]
            );
        }

        $joke = new \Chuck\Entity\Joke([
            'id'         => \Chuck\Util::createSlugUuid(),
            'value'      => $value,
            'categories' => ! empty($input->getArgCategory())
                ? $input->getArgCategory()
                : nulll
        ]);

        try {
            $joke = $jokeFacade->insert($joke);
        } catch (\Doctrine\DBAL\Exception\UniqueConstraintViolationException $exception) {
            $this->doLogging($exception->getMessage(), $request);

            return new \Symfony\Component\HttpFoundation\JsonResponse(
                [
                    'icon_url'      => self::$iconUrl,
                    'text'          => sprintf('Sorry dude %s , this joke already exists.', self::$shrug)
                ]
            );

        } catch (\Exception $exception) {
            $this->doLogging($exception->getMessage(), $request);

            return new \Symfony\Component\HttpFoundation\JsonResponse(
                [
                    'icon_url'      => self::$iconUrl,
                    'text'          => sprintf('Sorry dude %s , something went wrong here. Please check the syntax and try again.', self::$shrug)
                ]
            );
        }

        $this->doLogging(
            sprintf('Added new joke ("joke_id: %s").', $joke->getId()),
            $request
        );

        $attachments[] = [
            'title'     => sprintf('Id: %s', $joke->getId()),
            'text'      => $joke->getValue(),
            'mrkdwn_in' => [ 'text' ]
        ];

        return new \Symfony\Component\HttpFoundation\JsonResponse(
            [
                'icon_url'      => self::$iconUrl,
                'attachments'   => $attachments,
                'text'          => '*Joke was successfully saved!*',
                'mrkdwn'        => true
            ]
        );
    }

    /**
     *
     * @param  string $text
     * @param  \Symfony\Component\HttpFoundation\Request $request
     * @return void
     */
    protected function doLogging(
        $text,
        \Symfony\Component\HttpFoundation\Request $request
    ) {
        $this->logInfo(
            json_encode([
                'type'      => 'slack_command',
                'reference' => $request->headers->get('HTTP_X_REQUEST_ID', \Chuck\Util::createSlugUuid()),
                'meta'      => [
                    'request'  => [
                        'token'        => $request->get('token'),
                        'team_id'      => $request->get('team_id'),
                        'team_domain'  => $request->get('team_domain'),
                        'channel_id'   => $request->get('channel_id'),
                        'channel_name' => $request->get('channel_name'),
                        'user_id'      => $request->get('user_id'),
                        'user_name'    => $request->get('user_name'),
                        'command'      => $request->get('command'),
                        'text'         => $request->get('text'),
                        'response_url' => $request->get('response_url')
                    ],
                    'response' => [
                        'text' => $text
                    ]
                ]
            ])
        );
    }

    /**
     *
     * @param  \Chuck\JokeFacade $jokeFacade
     * @param  string $showCount
     * @return string
     */
    protected function getCategoriesText(\Chuck\JokeFacade $jokeFacade, $showCount = false)
    {
        $response = null;
        foreach ($categories = $jokeFacade->getCategories() as $category) {
            $response[] = $showCount
                ? sprintf('%s (%d)', $category['name'], $category['count'])
                : $category['name'];
        }

        if (! $showCount) {
            asort($response);
        }

        return sprintf(
            'Available categories are: `%s`. Type `/chuck {category_name}` to retrieve a random joke from within the given category.',
            implode('`, `', $response)
        );
    }

    /**
     *
     * @param \Chuck\App\Api\Model\SlackInput           $input
     * @param \Chuck\JokeFacade                         $jokeFacade
     * @param \Symfony\Component\HttpFoundation\Request $request
     * @return \Symfony\Component\HttpFoundation\JsonResponse
     */
    protected function doSearchCommand(
        \Chuck\App\Api\Model\SlackInput           $input,
        \Chuck\JokeFacade                         $jokeFacade,
        \Symfony\Component\HttpFoundation\Request $request
    ) {

        $offset = (int) ! empty($input->getArgStart())
            ? $input->getArgStart() - 1
            : 0;

        $attachments = [];
        $response    = $jokeFacade->searchByQuery(
            $query = $input->getInputWithoutArgs(),
            $limit  = 5,
            ! empty($offset) ? $offset : 0
        );

        if ($response['result']) {
            $showJokeId  = $input->hasIdArg();
            $showJokeCat = $input->hasCatArg();

            foreach ($response['result'] as $index => $joke) {
                $attachment = [
                    'title'     => sprintf('(%d)', $offset + $index + 1),
                    'text'      => $joke->getValue() . "\n",
                    'mrkdwn_in' => [
                        'text', 'pretext', 'fields'
                    ]
                ];

                if ($showJokeId) {
                    $attachment['fields'][] = [
                        'title' => 'Id',
                        'value' => $joke->getId(),
                        'short' => true
                    ];
                }

                if ($showJokeCat) {
                    $attachment['fields'][] = [
                        'title' => 'Category',
                        'value' => $joke->hasCategory()
                            ? implode(',', $joke->getCategories())
                            : '`none`',
                        'short' => true
                    ];
                }

                array_push($attachments, $attachment);
            }

            $text = sprintf(
                '*Search results: %s - %s of %s*.',
                0 === $offset ? 1 : $offset + 1,
                $shown = $offset + $index + 1,
                number_format($response['total'], 0, '.', ','),
                $query
            );

            if ($shown < $response['total']) {
                $text .= sprintf(
                    ' Type `/chuck ? %s --start %s` to see more results.',
                    $query,
                    $offset + $index + 2
                );
            }

        } else {
            $text = sprintf(
                'Your search for *"%s"* did not match any joke %s. Make sure that all words are spelled correctly. Try different keywords. Try more general keywords.',
                $query,
                self::$shrug
            );
        }

        $this->doLogging($text, $request);

        return new \Symfony\Component\HttpFoundation\JsonResponse(
            [
                'icon_url'      => self::$iconUrl,
                'response_type' => 'in_channel',
                'attachments'   => $attachments ? $attachments: null,
                'text'          => $text,
                'mrkdwn'        => true
            ],
            200,
            [
                'Access-Control-Allow-Origin'      => '*',
                'Access-Control-Allow-Credentials' => 'true',
                'Access-Control-Allow-Methods'     => 'GET, HEAD',
                'Access-Control-Allow-Headers'     => 'Content-Type, Accept, X-Requested-With'
            ]
        );
    }

    /**
     *
     * @param  \Silex\Application                        $app
     * @param  \Symfony\Component\HttpFoundation\Request $request
     * @return \Symfony\Component\HttpFoundation\JsonResponse
     */
    public function indexAction(
        \Silex\Application $app,
        \Symfony\Component\HttpFoundation\Request $request
    ) {
        $input = \Chuck\App\Api\Model\SlackInput::fromString($request->get('text'));
        $this->setLogger($app['monolog']);

        if ($input->isHelpMode()) {

            $attachments[] = [
                'title'     => 'Random joke',
                'text'      => 'Type `/chuck` to get a random joke.',
                'mrkdwn_in' => [ 'text' ]
            ];

            $attachments[] = [
                'title'     => 'Free text search',
                'text'      => 'Type `/chuck ? {search_term}` to search within tens of thousands Chuck Norris jokes.',
                'mrkdwn_in' => [ 'text' ]
            ];

            $attachments[] = [
                'title'     => 'Random joke from category',
                'text'      => 'Type `/chuck {category_name}` to get a random joke from within a given category.',
                'mrkdwn_in' => [ 'text' ]
            ];

            $attachments[] = [
                'title'     => 'Categories',
                'text'      => 'Type `/chuck -cat` to retrieve a list of all categories.',
                'mrkdwn_in' => [ 'text' ]
            ];

            $attachments[] = [
                'title'     => 'Get by id',
                'text'      => 'Type `/chuck : {joke_id}` to retrieve get a joke by a given `id`.',
                'mrkdwn_in' => [ 'text' ]
            ];

            $attachments[] = [
                'title'     => 'Help',
                'text'      => 'Type `/chuck help` to display a list of available commands.',
                'mrkdwn_in' => [ 'text' ]
            ];

            $this->doLogging(
                $text = '*Available commands:*',
                $request
            );

            return new \Symfony\Component\HttpFoundation\JsonResponse(
                [
                    'icon_url'      => self::$iconUrl,
                    'attachments'   => $attachments,
                    'text'          => $text,
                    'mrkdwn'        => true
                ],
                200,
                [
                    'Access-Control-Allow-Origin'      => '*',
                    'Access-Control-Allow-Credentials' => 'true',
                    'Access-Control-Allow-Methods'     => 'GET, HEAD',
                    'Access-Control-Allow-Headers'     => 'Content-Type, Accept, X-Requested-With'
                ]
            );
        }

        if ($input->isAddMode()) {
            return $this->doAddCommand($input, $app['chuck.joke'], $request);
        }

        if ($input->isEditMode()) {
            $category = $input->getArgCategory();
            $joke     = $input->getArgJoke();
            $id       = $input->getArgId();

            $this->doLogging(
                $text = sprintf('This lazy programmer still hasn\'t implemented this feature %s ...', self::$shrug),
                $request
            );

            return new \Symfony\Component\HttpFoundation\JsonResponse(
                [
                    'icon_url' => self::$iconUrl,
                    'text'     => $text
                ],
                200,
                [
                    'Access-Control-Allow-Origin'      => '*',
                    'Access-Control-Allow-Credentials' => 'true',
                    'Access-Control-Allow-Methods'     => 'GET, HEAD',
                    'Access-Control-Allow-Headers'     => 'Content-Type, Accept, X-Requested-With'
                ]
            );
        }

        if ($input->isSearchMode()) {
            return $this->doSearchCommand($input, $app['chuck.joke'], $request);
        }

        if ($input->isShowCategories()) {

            $text = $input->hasCountArg()
                ? $this->getCategoriesText($app['chuck.joke'], true)
                : $this->getCategoriesText($app['chuck.joke']);

            $this->doLogging($text, $request);

            return new \Symfony\Component\HttpFoundation\JsonResponse(
                [
                    'icon_url'      => self::$iconUrl,
                    'text'          => $text,
                    'mrkdwn'        => true
                ],
                200,
                [
                    'Access-Control-Allow-Origin'      => '*',
                    'Access-Control-Allow-Credentials' => 'true',
                    'Access-Control-Allow-Methods'     => 'GET, HEAD',
                    'Access-Control-Allow-Headers'     => 'Content-Type, Accept, X-Requested-With'
                ]
            );
        }

        if ($input->isGetByIdMode()) {

            if (null === $id = $input->getId()) {
                $text = sprintf(
                    'Sorry dude %s , the id ("%s") you\'ve entered is not valid.',
                    self::$shrug,
                    $input->getInputWithoutArgs()
                );
            }

            if ($input->getId()) {
                $joke = $app['chuck.joke']->get($id);

                if ($joke instanceof \Chuck\Entity\Joke) {
                    $text = $input->hasIdArg()
                        ? sprintf('%s `[ joke_id: %s ]`', $joke->getValue(), $joke->getId())
                        : $joke->getValue();
                } else {
                    $text = sprintf(
                        'Sorry dude %s , we\'ve found no jokes for the given id ("%s").',
                        self::$shrug,
                        $id
                    );
                }
            }

            return new \Symfony\Component\HttpFoundation\JsonResponse(
                [
                    'icon_url'      => self::$iconUrl,
                    'response_type' => 'in_channel',
                    'text'          => $text,
                    'mrkdwn'        => true
                ],
                200,
                [
                    'Access-Control-Allow-Origin'      => '*',
                    'Access-Control-Allow-Credentials' => 'true',
                    'Access-Control-Allow-Methods'     => 'GET, HEAD',
                    'Access-Control-Allow-Headers'     => 'Content-Type, Accept, X-Requested-With'
                ]
            );
        }

        $joke = $app['chuck.joke']->random(
            $category = $input->getInputWithoutArgs()
        );

        if ($category && ! $joke->getValue()) {
            $text = sprintf(
                'Sorry dude %s , we\'ve found no jokes for the given category ("%s"). Type `/chuck -cat` to see available categories or search by query `/chuck ? {search_term}`.',
                self::$shrug,
                $category
            );
        } else {
             $text = $input->hasIdArg()
                ? sprintf('%s `[ joke_id: %s ]`', $joke->getValue(), $joke->getId())
                : $joke->getValue();
        }

        $this->doLogging($text, $request);


        return new \Symfony\Component\HttpFoundation\JsonResponse(
            [
                'icon_url'      => self::$iconUrl,
                'response_type' => 'in_channel',
                'attachments'   => [
                    [
                        'fallback'   => $text,
                        'title'      => '[permalink]',
                        'title_link' => sprintf(
                            'https://api.chucknorris.io/jokes/%s?utm_source=slack&utm_medium=api&utm_term=%s&utm_campaign=random+joke',
                            $joke->getId(),
                            $joke->getId()
                        ),
                        'text'       => $text
                    ]
                ],
                'mrkdwn'        => true
            ],
            200,
            [
                'Access-Control-Allow-Origin'      => '*',
                'Access-Control-Allow-Credentials' => 'true',
                'Access-Control-Allow-Methods'     => 'GET, HEAD',
                'Access-Control-Allow-Headers'     => 'Content-Type, Accept, X-Requested-With'
            ]
        );
    }
}
