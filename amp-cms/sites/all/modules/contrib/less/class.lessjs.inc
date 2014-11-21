<?php

/**
 * @file
 * Contains 'lessjs' class; an abstraction layer for command line less.js.
 */

/**
 * 'lessjs' class.
 */
class Lessjs {
  
  // Base command is hardcoded here to reduce security vulnerability.
  private static $base_command = 'lessc';
  
  protected $input_file = NULL;
  
  // @link http://lesscss.org/usage/#command-line-usage-include-paths
  protected $include_paths = array();
  
  // @link http://lesscss.org/usage/#command-line-usage-global-variable
  protected $global_variables = array();
  
  protected $source_maps_enabled = FALSE;
  
  // @link http://lesscss.org/usage/#command-line-usage-source-map-rootpath
  protected $source_map_rootpath = NULL;
  
  // @link http://lesscss.org/usage/#command-line-usage-source-map-basepath
  protected $source_map_basepath = NULL;
  
  // Holds value for current error.
  protected $error = NULL;
  
  /**
   * Constructor function for 'lessjs'.
   * 
   * @param string $input_file
   *   Path for .less file relative to getcwd().
   */
  public function __construct($input_file = NULL) {
    
    $this->input_file = $input_file;
  }
  
  /**
   * Returns the version string from command line less.js.
   * 
   * @return string
   *   Version string from less.js.
   */
  public function version() {
    
    return $this->proc_open(array('--version'));
  }
  
  /**
   * Returns the current error if one exists.
   * 
   * @return string
   *   Error string returned by command line less.js.
   */
  public function get_error() {
    return $this->error;
  }
  
  /**
   * Add include path that will be set with '--include-path' argument.
   * 
   * @link http://lesscss.org/usage/#command-line-usage-include-paths
   * 
   * @param string $include_path
   *   Path relative to getcwd().
   */
  public function include_path($include_path) {
    
    $this->include_paths[] = $include_path;
    
  }
  
  /**
   * Add LESS variable that will be set with the '--global-var' argument.
   * 
   * @param string $variable_name
   *   The variable name.
   * @param string $variable_value
   *   The variable value.
   */
  public function global_var($variable_name, $variable_value) {
    
    $global_variables[$variable_name] = $variable_value;
  }
  
  /**
   * Enable source maps for current file, and configure source map paths.
   * 
   * @param bool $enabled
   *   Set the source maps flag.
   * @param string $rootpath
   *   Value to be prepended to each source map URL.
   *   @link http://lesscss.org/usage/#command-line-usage-source-map-rootpath
   * @param string $basepath
   *   Leading value to be stripped from each source map URL.
   *   @link http://lesscss.org/usage/#command-line-usage-source-map-basepath
   */
  public function source_maps($enabled, $rootpath = NULL, $basepath = NULL) {
    $this->source_maps_enabled = $enabled;
    
    $this->source_map_rootpath = $rootpath;
    $this->source_map_basepath = $basepath;
  }
  
  /**
   * Provides list to command line arguments for execution.
   * 
   * @return array
   *   Array of command line arguments.
   */
  private function command_arguments() {
    
    $arguments = array();
    
    // Add include paths.
    if (count($this->include_paths) > 0) {
      
      $arguments[] = '--include-path=' . implode(PATH_SEPARATOR, array_map('escapeshellarg', $this->include_paths));
      
      // @link http://lesscss.org/usage/#command-line-usage-relative-urls
      $arguments[] = '--relative-urls';
    }
    
    // Add any defined variables.
    foreach ($this->global_variables as $global_variable_name => $global_variable_value) {
      
      // @link http://lesscss.org/usage/#command-line-usage-global-variable
      $arguments[] = '--global-var=' . escapeshellarg($global_variable_name . '=' . $global_variable_value);
    }
    
    // Set service map flags.
    if ($this->source_maps_enabled) {
      
      if (isset($this->source_map_rootpath)) {
        
        $arguments[] = '--source-map-rootpath=' . escapeshellarg($this->source_map_rootpath);
      }
      
      if (isset($this->source_map_basepath)) {
        
        $arguments[] = '--source-map-basepath=' . escapeshellarg($this->source_map_basepath);
      }
      
      // @link http://lesscss.org/usage/#command-line-usage-source-map-map-inline
      $arguments[] = '--source-map-map-inline';
    }
    
    // Input file should be last argument.
    // @link http://lesscss.org/usage/#command-line-usage-command-line-usage
    $arguments[] = $this->input_file;
    
    return $arguments;
  }
  
  /**
   * Returns list of files that input file depends on.
   * 
   * @return array
   *   List of @import'ed files.
   */
  public function depends() {
    
    $output_key = 'depends';
    
    $depends_arguments = array();
    
    $depends_arguments[] = '--depends';
    
    $depends_arguments[] = $output_key;
    
    $depends_files_spaced = $this->proc_open(array_merge($this->command_arguments(), $depends_arguments));
    
    // {$output_key}: /path/to/file/1 /path/to/file/2
    $depends_files_spaced = str_replace($output_key . ':', '', $depends_files_spaced);
    
    return explode(' ', trim($depends_files_spaced));
  }
  
  /**
   * Executes compilation of LESS input.
   * 
   * @return string
   *   Compiled CSS.
   */
  public function compile() {
    
    return $this->proc_open($this->command_arguments());
  }
  
  private function proc_open($command_arguments = array(), $stdin = NULL) {
    
    $output_data = NULL;
    
    $command = implode(' ', array_merge(array(self::$base_command), $command_arguments));
    
    // Handles for data exchange.
    $pipes = array(
      0 => NULL, // STDIN
      1 => NULL, // STDOUT
      2 => NULL, // STDERR
    );
    
    // Sets permissions on $pipes.
    $descriptors = array(
      0 => array('pipe', 'r'), // STDIN
      1 => array('pipe', 'w'), // STDOUT
      2 => array('pipe', 'w'), // STDERR
    );
    
    $process = proc_open($command, $descriptors, $pipes);
    
    if (is_resource($process)) {
      
      // STDIN not currently used in LESS module.
      if ($this->input_file === '-' && isset($stdin)) {
        fwrite($pipes[0], $stdin);
      }
      fclose($pipes[0]); // fclose() on STDIN executes $command, if program is expecting input from STDIN.
      
      $output_data = stream_get_contents($pipes[1]);
      fclose($pipes[1]);
      
      $this->error = stream_get_contents($pipes[2]);
      fclose($pipes[2]);
      
      proc_close($process);
    }
    
    return $output_data;
  }
}
