\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{gts2012}

%-------------------- This is based on the article class ----------------

%\PassOptionsToClass{twocolumn}{article}
\PassOptionsToClass{twoside}{article}
\PassOptionsToClass{a4paper}{article}

\LoadClass{article}

\RequirePackage{theorem,graphicx,amssymb,amsmath,fancyhdr}

%------------------ Defining the most common environments ---------------

\theorembodyfont{\slshape}

\newtheorem{theorem}{Theorem}
\newtheorem{lemma}[theorem]{Lemma}
\newtheorem{cor}[theorem]{Corollary}
\newtheorem{prop}[theorem]{Proposition}
\newtheorem{invar}{Invariant}
\newtheorem{obs}{Observation}
\newtheorem{conj}{Conjecture}
\newtheorem{defini}{Definition}

\def\QED{\ensuremath{{\square}}}
\def\markatright#1{\leavevmode\unskip\nobreak\quad\hspace*{\fill}{#1}}
\newenvironment{proof}
 {\begin{trivlist}\item[\hskip\labelsep{\bf Proof.}]}
 {\markatright{\QED}\end{trivlist}}

\newenvironment{acknowledgments}%
{\@startsection{acknowledgments}{4}{\z@}{3.25ex plus 1ex minus .2ex}%
  {-1em}{\small{\sf\bfseries{}Acknowledgments.}}\@titlestyle\small}%
{\normalsize}

%------------------------------------------------------------------------

\setlength{\textheight}{9.50in}
\setlength{\textwidth}{6.75in}
\setlength{\topmargin}{-0.40in}
\setlength{\oddsidemargin}{-0.125in}
\setlength{\evensidemargin}{-0.375in}
\setlength{\columnsep}{2pc}
\setlength{\headheight}{0.200in}
\setlength{\headsep}{0.4in}
\setlength{\footskip}{0.450in}
\setlength{\parindent}{1em}
\flushbottom

\font\tensfb = cmssbx10
\newcommand{\@titlestyle}{\normalsize\tensfb}

\let\ps@plain\ps@fancy

\renewcommand{\@maketitle}{%
 \newpage
 \null
 \vskip 2em%
 \begin{center}%
   \let \footnote \thanks
   {\vspace{-.2in}\Large\sffamily\bfseries \@title \par}%
   \vskip 1.5em%
   {\normalsize
     \lineskip .5em%
     \begin{tabular}[t]{c}%
       \@author
     \end{tabular}\par}%
   \vskip 1em%
   {\large \@date}%
 \end{center}%
 \par
 \vskip 3em}

\renewenvironment{theindex}
              {\if@twocolumn
                 \@restonecolfalse
               \else
                 \@restonecoltrue
               \fi
               \columnseprule \z@
               \columnsep 35\p@
               \twocolumn[\section*{\indexname}]%
               \@mkboth{\MakeUppercase\indexname}%
                       {\MakeUppercase\indexname}%
               \thispagestyle{fancy}\parindent\z@
               \parskip\z@ \@plus .3\p@\relax
               \let\item\@idxitem}
              {\if@restonecol\onecolumn\else\clearpage\fi}  

\renewcommand\section{\@startsection {section}{1}{\z@}{-3.5ex plus
   -1ex minus -.2ex}{2.3ex plus .2ex}{\@titlestyle}}
\renewcommand\subsection{\@startsection{subsection}{2}{\z@}{-3.25ex
   plus -1ex minus -.2ex}{1.5ex plus .2ex}{\@titlestyle}}
\renewcommand\subsubsection{\@startsection{subsubsection}{3}{\z@}{-3.25ex
   plus -1ex minus -.2ex}{1.5ex plus .2ex}{\@titlestyle}}
\renewcommand\paragraph{\@startsection {paragraph}{4}{\z@}{3.25ex plus
   1ex minus .2ex}{-1em}{\@titlestyle}}
\renewcommand\subparagraph{\@startsection
 {subparagraph}{4}{\parindent}{3.25ex plus 1ex minus
   .2ex}{-1em}{\@titlestyle}}

\renewcommand\abstract{\if@twocolumn
\section*{Abstract}
\else \small
\begin{center}
{\sffamily\bfseries Abstract\vspace{-.5em}\vspace{0pt}}
\end{center}
\quotation
\fi}

\def\endabstract{\if@twocolumn\else\endquotation\fi}

\date{}

\renewcommand{\textfraction}{0.05}
\renewcommand{\bottomfraction}{0.95}
\renewcommand{\topfraction}{0.95}
\renewcommand{\dbltopfraction}{0.95}
\renewcommand{\dblfloatpagefraction}{0.8}
\setcounter{topnumber}{3}

\newcommand{\l@day}{\@dottedtocline{1}{0em}{0em}}
\newcommand{\l@session}{\@dottedtocline{1}{0em}{1in}}
\newcommand{\l@algorithm}{\@dottedtocline{1}{0em}{1in}}
\renewcommand{\@dotsep}{500}

%--------------------------- Headers and Footers ------------------------

\fancypagestyle{plain}{
\fancyhead[RO]{\sf GTS 2012, Chapel Hill, NC, United States, June 17 \&
  19, 2012}
\fancyhead[LE]{\sf Minisymposium on Publicly Available
  Geometric/Topological Software, 2012}
\fancyfoot[L]{\sf \scriptsize{This is an abstract related to tutorial given at GTS 2012. It has been made public for the benefit of the community and should be considered a preprint rather than a formally reviewed paper. Thus, this work is expected to appear in a conference with formal proceedings and/or in a journal.}}}



\pagestyle{fancy}
\fancyhead{}
\fancyfoot{}
\fancyhead[RO]{\sf GTS 2012, Chapel Hill, NC, United States, June 17 \&
  19, 2012}
\fancyhead[LE]{\sf Minisymposium on Publicly Available
  Geometric/Topological Software, 2012}


\renewcommand{\headrulewidth}{1pt}

\endinput