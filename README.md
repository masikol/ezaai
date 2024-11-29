# EzAAI: High Throughput Prokaryotic AAI Calculator.

[![License](https://img.shields.io/github/license/endixk/ezaai)](https://github.com/endixk/ezaai/blob/main/LICENSE.md)

EzAAI is a suite of workflows for improved AAI calculation performance along with the novel module that provides hierarchical clustering analysis and dendrogram representation.

 * [Homepage](http://leb.snu.ac.kr/ezaai)
 * [Publication](https://doi.org/10.1007/s12275-021-1154-0)

## Masikol version

I forked the [original EzAAI](https://github.com/endixk/ezaai) `v1.2.3` in order to change `mmseqs2` parallelization strategy in `ezaai calculate`. The original EzAAI processes all genome pairs consecutively, and multiprocessing is implemented as passing a `-t` argument to `mmseqs2`. Unfortunately, this strategy doesn’t speed up calculation much.

My version, `v1.2.3_masikol_0.1`, can spawn multiple parallel `mmseqs2` processes so that multiple genome pairs can be compared simultaneously. Each spawned `mmseqs2` runs using single thread. As in the original EzAAI, one specifies the number of threads using `-t` option of the program `ezaai calculate`.

Limitations of the masikol version:

- `ezaai calculate` can use only mmseqs2 for sequence alignment. Blastp and diamond are disabled.
- Option `-match` is disabled in `ezaai calculate`.

See [masikol version instaltion](#masikol-version-installation) and an [example](#masikol-version-example) below.

## Quick start with conda

~~~bash
conda install -c bioconda -y ezaai
ezaai -h
~~~

## Available modules
### `extract`
 * Extract protein database from genome using Prodigal

~~~bash
ezaai extract -i <IN_SEQ> -o <OUT_DB>
~~~

|Argument|Description|
|:-:|-----------------|
|`-i`|Input prokaryotic genome sequence|
|`-o`|Output database|

---

### `calculate`	
 * Calculate AAI value from protein databases using MMseqs2

~~~bash
ezaai calculate -i <INPUT_1> -j <INPUT_2> -o <OUTPUT>
~~~

|Argument|Description|
|:-:|-----------------|
|`-i`|First input DB / directory with DBs|
|`-j`|Second input DB / directory with DBs|
|`-o`|Output result file|

---

### `convert`	
 * Convert CDS FASTA file into MMseqs2 database

~~~bash
ezaai convert -i <IN_CDS> -s <SEQ_TYPE> -o <OUT_DB>
~~~

|Argument|Description|
|:-:|-----------------|
|`-i`|Input CDS profile (FASTA format)|
|`-s`|Sequence type of input file (nucl/prot)|
|`-o`|Output database|

---

### `cluster`
 * Hierarchical clustering of taxa with AAI values

~~~bash
ezaai cluster -i <AAI_TABLE> -o <OUTPUT>
~~~

|Argument|Description|
|:-:|-----------------|
|`-i`|Input EzAAI result file containing all-by-all pairwise AAI values|
|`-o`|Output result file|


## Masikol version installation

1. Clone the repo or download a ZIP-archive and unpack it.

2. Go to the `ezaai` directory.

3. Build an executable jar with Maven: `mvn clean package`. This will produce a `target/` directory with the desired jar in it: `EzAAI-1.2.3_masikol.0.1-jar-with-dependencies.jar`.

4. Execute `ezaai calculate` as follows: `java -jar target/EzAAI-1.2.3_masikol.0.1-jar-with-dependencies.jar calculate [...]`

## Masikol version example

Activate you original `ezaai` conda environment as usual to use its dependencies:

```bash
conda activate ezaai
```
Then run `ezaai calculate` as follows:

```bash
java -jar target/EzAAI-1.2.3_masikol.0.1-jar-with-dependencies.jar calculate \
    -i Bacillaceae_genomes/ezaai-db \
    -j Bacillaceae_genomes/ezaai-db \
    -t 72 \
    -self 1 \
    -o aai_result.tsv
```