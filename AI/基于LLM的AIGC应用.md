# 基于LLM的AIGC应用  
Artificial Intelligence Generated Content，生成式人工智能AIGC

## NLP
Natural Language Processing，自然语言处理
### NLP和LLM关系
+ 早期：依赖规则来处理语言，即根据语法和语义规则来解析和生成语句。局限性较大，难以应对语言的复杂性。
+ 中期：随着机器学习和深度学习的发展，开始采用基于统计的方法。可以通过大量的语料库学习语言的统计规律，提高模型的性能。但这些方法仍然受到模型容量和数据稀疏性的限制。
+ LLM时代：大语言模型通过使用深度神经网络结构和海量数据的训练，可以自动学习语言的规律和模式，生成具有语言风格和逻辑性的语句。

### Transformer
```
NLP主要技术，基于Attention注意力机制。是一种神经网络，通过跟踪序列数据中的关系（如这句话中的单词）来学习上下文并因此学习含义。   
在许多情况下，Transformer正在取代卷积和循环神经网络（CNN和RNN）
```
```
缺点：只是限于单一模态（文本），不能迁移CV图像领域的主要原因在于输入长度限制
```

## LLM大模型
Large Language Models，大语言模型 

### LLM工作原理
```
1、使用自监督学习（self-supervised learning）的方法来训练
2、不需要人工标注的数据来初始化模型参数，通常是由模型自己生成的伪标签。比如根据文本的一部分来预测另一部分
3、然后再利用有监督或无监督的方法来进行实际任务的学习。比如根据文本的语义来分类或生成。
```

### LLM模型
+ GPT系列：Generative Pre-trained Transformer
+ BERT系列：Bidirectional Encoder Representations from Transformers
+ T5系列：Text-to-Text Transfer Transformer

## 多模态大模型
融合NLP和CV，将文本，图像，语音等不同类型的数据，放到同一个特征空间去表示，好处是可以将不同类型数据打通，在一个任务上利用到更多更全面的数据，来提升业务指标的效果。例如以文搜图。

## AIGC模型
### 生成模型
+ GAN：Generate Adversarial Network，生成式对抗网络
```
将两个神经网络进行对抗，即生成器（Generative Model）与鉴别器（Discriminative Model）。生成器用于生成“造假数据”，鉴别器用于判断数据的真伪，在二者的对抗博弈中，最终二者达到了平衡，用于图像、视频、语音的合成生成。
```
### 扩散模型
+ DDPM： Denoising Diffusion Probabilistic Models

## 名词解释
```
SOTA模型：state of the art，不是特指某个具体的模型，而是指在该项研究任务中，目前最好/最先进的模型
```

## 参考
[构筑大语言模型应用：应用开发与架构设计](https://aigc.phodal.com/)