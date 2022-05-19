# nifi-parsejsonattribute

Apache Nifi Parse Json Attribute Processor

**Please contribute project.**

# Get Started! :sunglasses:

Download [stable version](https://github.com/guvencenanguvenal/nifi-parsejsonattribute/releases/tag/stable).

Build project 

```yaml
mvn clean install
```

After build success, COPY `attribute-parse-json.nar` file from `target` directory to `{NIFI_PATH}/lib` directory.

# How to Use? :star:

 -  After copy nar file, restart nifi and you can add ParseJsonAttribute processor like other processors.
 -  You must fill properties.
 
**Json Attribute**

Property is which property is a json.

**Json Properties**

Property is which is created after parsing.

for this example attribute should be JSON1,JSON2,JSON3: 

```yaml

{
  'JSON1': 1,
  'JSON2': 2,
  'JSON3': 3
}
```

