class Class
  def thread_local_accessor name, options = {}
    m = Module.new
    m.module_eval do
      class_variable_set :"@@#{name}", Hash.new {|h,k| h[k] = options[:default] }
    end
    m.module_eval %{
      FINALIZER = lambda {|id| @@#{name}.delete id }

      def #{name}
        @@#{name}[Thread.current.object_id]
      end

      def #{name}=(val)
        ObjectSpace.define_finalizer Thread.current, FINALIZER  unless @@#{name}.has_key? Thread.current.object_id
        @@#{name}[Thread.current.object_id] = val
      end
    }

    class_eval do
      include m
      extend m
    end
  end
end
