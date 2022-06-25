botright new
:q!
let g:compP = ''
let g:compF = ''
set ignorecase
set tabstop=4
set autoindent 
syntax on
set expandtab
set softtabstop=4
set bg=dark
colorscheme papercolor-theme
set laststatus=2
set statusline=%F
set statusline+=%=
set statusline+=%{getcwd()}
set number
set autochdir
set tabstop=4
set autoindent
let g:netrw_banner = 0
let g:netrw_liststyle = 3
let g:netrw_browse_split = 4
let g:netrw_altv = 1
set expandtab
set softtabstop=4
set bg=dark
set clipboard=unnamed
imap <Up> <nop>
imap <Left> <nop>
imap <Down> <nop>
imap <Right> <nop>
map <Up> <nop>
map <Left> <nop>
map <Down> <nop>
map <Right> <nop>
nmap t mz:s/\v^/    <CR>`z4l
nmap ? mz:s/\v\/\//<CR>`z2h
nmap , mz:s/\v^/\/\/<CR>`z2l
inoremap jk <Esc>
onoremap jk <Esc>
vnoremap jk <Esc>
nnoremap ; mqA;<Esc>q<Esc><Esc>
set backspace=indent,eol,start
command! -nargs=0 Reload so $MYVIMRC
noremap <Leader>y "*y
noremap <Leader>p "*p
noremap <Leader>Y "+y
noremap <Leader>P "+p;
command! -nargs=0 Pcomp call Pcompf()
fu! Pcompf()
    vnew 
    r !/home/ibrahim/robotics/SwerveLib/runScript
endfunction

