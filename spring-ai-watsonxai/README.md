## spring-ai-zhipuai

### 项目介绍

spring-ai-zhipuai 基于 Spring Boot 3.x 、Spring AI 和 [智普AI](https://open.bigmodel.cn/) 提供的 ChatGLM 大语言模型提供的智能对话能力，支持对话问答、文本生成等功能。


### 智普AI

智谱AI 开放平台提供一系列具有不同功能和定价的大模型，包括通用大模型、超拟人大模型、图像大模型、向量大模型等，并且支持使用您的私有数据对模型进行微调。

2024年01月16日，我们在「智谱AI技术开放日(ZHIPU DevDay)」推出新一代基座大模型 GLM-4。

- 官网地址：[https://open.bigmodel.cn/](https://open.bigmodel.cn/)
- API文档：[https://open.bigmodel.cn/dev/api](https://open.bigmodel.cn/dev/api)

#### 资源

- 查看模型[接口文档](https://open.bigmodel.cn/dev/api)
- 体验模型能力[体验中心](https://open.bigmodel.cn/trialcenter)
- 查看您的 [API Keys](https://open.bigmodel.cn/usercenter/apikeys)
- 构建[知识库](https://open.bigmodel.cn/knowledge)
- 创建大模型应用[应用中心](https://open.bigmodel.cn/appcenter/myapp)

#### 关键概念

##### GLM

> GLM 全名 General Language Model ，是一款基于自回归填空的预训练语言模型。ChatGLM 系列模型，支持相对复杂的自然语言指令，并且能够解决困难的推理类问题。该模型配备了易于使用的 API 接口，允许开发者轻松将其融入各类应用，广泛应用于智能客服、虚拟主播、聊天机器人等诸多领域。

##### Embedding

> Embedding 是一种将数据（如文本）转化为向量形式的表示方法，这种表示方式确保了在某些特定方面相似的数据在向量空间中彼此接近，而与之不相关的数据则相距较远。通过将文本字符串转换为向量，使得数据能够有效用于搜索、聚类、推荐系统、异常检测和分类等应用场景。

##### Token

> Token 是模型用来表示自然语言文本的基本单位，可以直观的理解为“字”或“词”；通常 1 个中文词语、1 个英文单词、1 个数字或 1 个符号计为 1 个token。 一般情况下 ChatGLM 系列模型中 token 和字数的换算比例约为 1:1.6 ，但因为不同模型的分词不同，所以换算比例也存在差异，每一次实际处理 token 数量以模型返回为准，您可以从返回结果的 usage 中查看。

#### 模型

智谱AI 开放平台提供了包括通用大模型、图像大模型、超拟人大模型、向量大模型等多种模型。

| 模型 |  描述 |
| ------------ | ------------ |
| GLM-4  | 提供了更强大的问答和文本生成能力。适合于复杂的对话交互和深度内容创作设计的场景，支持工具调用（ Function Call、Retrieval、Web_search ）。  |
| GLM-3-Turbo  | 适用于对知识量、推理能力、创造力要求较高的场景，比如广告文案、小说写作、知识类写作、代码生成等，支持工具调用（ Function Call、Retrieval、Web_search ）。  |
| CharGLM-3  | 基于人设的角色扮演的语言模型。  |
| CogView-3  | 适用多种图像生成任务，通过对用户文字描述快速、精准的生成图像。  |
| Embedding-2	  |  将输入的文本信息进行向量化表示。 |
