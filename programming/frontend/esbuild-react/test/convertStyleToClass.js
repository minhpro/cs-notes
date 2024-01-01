const INLINE_TO_CLASS = [
  {
    key: /;background-color:#23241f;color:#f8f8f2;overflow:visible;padding: 0.5rem 1.5rem;border-radius: 5px/g,

    value: 'ql-syntax',
  },
  {
    key: /;padding-left:3em;/g,
    value: 'ql-indent-1',
  },
  {
    key: /;padding-left:6em;/g,
    value: 'ql-indent-2',
  },
  {
    key: /;padding-left:9em;/g,
    value: 'ql-indent-3',
  },
  {
    key: /;padding-left:12em;/g,
    value: 'ql-indent-4',
  },
  { key: /;padding-left:15em;/g, value: 'ql-indent-5' },
  {
    key: /;padding-left:18em;/g,
    value: 'ql-indent-6',
  },
  {
    key: /;padding-left:21em;/g,
    value: 'ql-indent-7',
  },
  {
    key: /;padding-left:24em;/g,
    value: 'ql-indent-8',
  },
];

function convertInlineStyleToClass(partContent) {
  const hasAttrElementRegex = /<\w+(?:\s+(?:\w*)\s*=\s*"[^"]*")+\s*>/gi;
  const classAttrRegExp = /class\s*=\s*"([^"]*)"/;
  const styleAttrRegExp = /style\s*=\s*"([^"]*)"/;

  let newPartContent = partContent;

  const styleReplacer = (content) => {
    const styleAttrMatch = content.match(styleAttrRegExp);

    if (!styleAttrMatch) {
      return content;
    }

    // get style to convert
    const styleConverted = [];
    let styleNotConvert = styleAttrMatch[1];
    INLINE_TO_CLASS.forEach((el) => {
      if (styleAttrMatch[1].match(el.key)) {
        styleConverted.push(el.value);
        // remove style to be convert from existing style attribute
        styleNotConvert = styleNotConvert.replace(el.key, '').trim();
      }
    });

    const convertedClass = styleConverted.join(' ');

    const classAttrMatch = content.match(classAttrRegExp);
    let tmpContent = '';
    let newContent = content;

    if (classAttrMatch) {
      if (styleNotConvert.length) {
        tmpContent = content.replace(styleAttrMatch[0], `style="${styleNotConvert}"`);
      } else {
        tmpContent = content.replace(styleAttrMatch[0], '');
      }
      newContent = tmpContent.replace(
        classAttrMatch[0],
        `class="${classAttrMatch[1].trim()} ${convertedClass}"`
      );
    } else {
      if (styleNotConvert.length) {
        if (convertedClass.trim().length > 0) {
          newContent = content.replace(
            styleAttrMatch[0],
            `class="${convertedClass}" style="${styleNotConvert}"`
          );
        }
      } else {
        newContent = content.replace(styleAttrMatch[0], `class="${convertedClass}"`);
      }
    }

    return newContent;
  };

  return newPartContent.replace(hasAttrElementRegex, styleReplacer);
}

var htmlContent = `
<p><strong>ここにコンテンツを記述ください。acscascsacacsacac</strong></p>
<p><br></p>
<p><code style=\"font-size:85%;color:#e83e8c;background-color:#f0f0f0;padding: 2px 4px;border-radius: 3px;font-size:
    85%;\"><strong>heklo</strong></code></p>
<p><br></p>
<blockquote style=\";border-left: 4px solid #ccc;margin-bottom: 5px;margin-top: 5px;margin-left:0;padding-left:16px\">
  are you ready ?</blockquote>
<ol>
  <li>helo 1</li>
  <li>helo2</li>
</ol>
<pre style=\";background-color:#23241f;color:#f8f8f2;overflow:visible;padding: 0.5rem 1.5rem;border-radius: 5px\"
  spellcheck=\"false\">đâsd\n</pre>
`

var newContent = convertInlineStyleToClass(htmlContent);

console.log(newContent);