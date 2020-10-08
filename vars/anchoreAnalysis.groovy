/**
 * Anchore analisis service using the Jenkins anchore plugin
 * @param registry : registry to upload the docker image
 * @param policy : policy to apply
 * @param dockerList : dockerList to analize
 * @return
 */
def anchoreAnalysis(String registry, String policy, def dockerList) {
    try {
        echo "anchore Condition in"
        def anchoreImagesFile = dockerList.collect {
            "${registry}/${it.image} ${it.file}"
        }.join("\n")        
        writeFile(file: 'anchore_images', text: anchoreImagesFile)
        anchore(name: 'anchore_images', engineRetries: "${util.getTimeout().toInteger() * 60}", policyBundleId: "${policy}", forceAnalyze: true)
        sh("rm anchore_images")
    }catch (Throwable ex){
        echo "error during Anchore Analisis"
        throw new AnchoreToolException("error during Anchore Analisis")
    }
}
