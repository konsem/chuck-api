import org.gradle.api.GradleException

class Git {

    private static final VERSION_2 = "git version 2"

    static String branch() throws GradleException {
        def gitBranch = execute('git rev-parse --abbrev-ref HEAD')

        if (gitBranch == null || gitBranch.empty) {
            throw new GradleException('Could not determine Git branch name')
        }

        return gitBranch.replace("/", "-")
    }

    static int commitCount() {
        return execute('git rev-list --count HEAD').toInteger()
    }

    static int commitDateUnix() {
        return execute(
                "git show -s --format=%ct ${commitHash()}"
        ).toInteger()
    }

    static String commitHash() {
        return execute('git rev-parse HEAD')
    }

    static String commitHashShort() {
        return execute('git rev-parse --short HEAD').substring(0, 7)
    }

    static String remoteOriginUrl() {
        return execute('git config --get remote.origin.url')
    }

    static void writePropertiesTo(pathname) {
        def properties = new Properties()

        properties.setProperty('git.branch', branch())
        properties.setProperty('git.commit_count', commitCount().toString())
        properties.setProperty('git.commit_date_unix', commitDateUnix().toString())
        properties.setProperty('git.commit_long', commitHash())
        properties.setProperty('git.commit_short', commitHashShort())
        properties.setProperty('git.remote_origin_url', remoteOriginUrl())

        def file = new File(pathname)
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs()
        }

        file.withWriter('UTF-8') {
            properties.each { key, value -> it.writeLine("$key = $value") }
        }
    }

    static boolean isAvailable() {
        try {
            return execute("git --version").startsWith(VERSION_2)
        } catch (ignored) {
            return false
        }
    }

    private static String execute(String command) {
        return command.execute().in.text.trim()
    }
}
